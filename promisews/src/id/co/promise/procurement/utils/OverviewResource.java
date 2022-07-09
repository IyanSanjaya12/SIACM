package id.co.promise.procurement.utils;

/**
 * @author : Reinhard
 */

import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.menu.AksiSession;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.core.ResourceInvoker;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.spi.metadata.ResourceMethod;
import org.jboss.resteasy.core.ResourceMethodRegistry;
import org.jboss.resteasy.util.WeightedMediaType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Stateless
@Path(value = "/promise/allservices")
public class OverviewResource {

	@EJB
	AksiSession aksisession;

	private static final class ParamDescription {
		private String paramType;
		private String variableType;
		private String name;

		public ParamDescription(String paramType, String variableType,
				String name) {
			super();
			this.paramType = paramType;
			this.variableType = variableType;
			this.name = name;

		}
	}

	private static final class MethodDescription {
		private String method;
		private String fullPath;
		private String cleanPath;
		private String produces;
		private String consumes;
		private List<ParamDescription> paramDescriptions;

		public MethodDescription(String method, String fullPath,
				String produces, String consumes,
				List<ParamDescription> paramDescriptions) {
			super();
			this.method = method;
			this.fullPath = fullPath;
			this.cleanPath = cleaningPath(fullPath);
			this.produces = produces;
			this.consumes = consumes;
			this.paramDescriptions = paramDescriptions;
		}

	}

	private static String cleaningPath(String path) {
		Integer pos = path.indexOf("{");
		String cleanPath = path;
		if (pos >= 0) {
			cleanPath = path.substring(0, pos - 1);
		}
		return cleanPath;
	}

	private static final class ResourceDescription {
		private String basePath;
		private List<MethodDescription> calls;

		public ResourceDescription(String basePath) {
			this.basePath = basePath;
			this.calls = Lists.newArrayList();
		}

		public void addMethod(String path, ResourceMethodInvoker method,
				List<ParamDescription> paramDescriptions) {
			String produces = mostPreferredOrNull(method.getProduces());
			String consumes = mostPreferredOrNull(method.getConsumes());

			for (String verb : method.getHttpMethods()) {
				calls.add(new MethodDescription(verb, path, produces, consumes,
						paramDescriptions));
			}
		}

		private static String mostPreferredOrNull(
				MediaType[] preferred) {
			if (preferred.length == 0) {
				return null;
			} else {
				return preferred.toString();
			}
		}

		public static List<ResourceDescription> fromBoundResourceInvokers(
				Set<Map.Entry<String, List<ResourceInvoker>>> bound) {
			Map<String, ResourceDescription> descriptions = Maps.newHashMap();

			for (Map.Entry<String, List<ResourceInvoker>> entry : bound) {
				Method aMethod = ((ResourceInvoker) entry.getValue().get(0)).getMethod();
				String basePath = aMethod.getDeclaringClass()
						.getAnnotation(Path.class).value();

				if (!descriptions.containsKey(basePath)) {
					descriptions.put(basePath,
							new ResourceDescription(basePath));
				}

				for (ResourceInvoker invoker : entry.getValue()) {
					ResourceMethodInvoker method = (ResourceMethodInvoker) invoker;

					String subPath = null;
					for (java.lang.annotation.Annotation annotation : method
							.getMethod().getAnnotations()) {

						if (annotation.annotationType().equals(Path.class)) {
							subPath = ((javax.ws.rs.Path) annotation).value();
							break;
						}
					}

					List<ParamDescription> paramDescriptions = new ArrayList<OverviewResource.ParamDescription>();
					for (Annotation[] annotations : method.getMethod()
							.getParameterAnnotations()) {
						for (Annotation annotation : annotations) {

							String paramName = null;

							if (annotation.annotationType().equals(
									PathParam.class)) {
								PathParam pathParam = (PathParam) annotation;
								paramName = pathParam.value();
							}

							if (annotation.annotationType().equals(
									FormParam.class)) {
								FormParam formParam = (FormParam) annotation;
								paramName = formParam.value();
							}
							if (annotation.annotationType().equals(
									QueryParam.class)) {
								QueryParam queryParam = (QueryParam) annotation;
								paramName = queryParam.value();
							}

							ParamDescription param = new ParamDescription(
									annotation.annotationType().getSimpleName(),
									"varType", paramName);
							paramDescriptions.add(param);

						}
					}

					descriptions.get(basePath).addMethod(basePath + subPath,
							method, paramDescriptions);

				}
			}

			return Lists.newLinkedList(descriptions.values());
		}
	}

	@GET
	@Path("/view")
	@Produces(MediaType.TEXT_HTML)
	public Response getAvailableEndpointsHtml(@Context Dispatcher dispatcher) {

		StringBuilder sb = new StringBuilder();
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher
				.getRegistry();
		List<ResourceDescription> descriptions = ResourceDescription
				.fromBoundResourceInvokers(registry.getBounded()
						.entrySet());

		sb.append("<h1>").append("REST interface overview").append("</h1>");

		for (ResourceDescription resource : descriptions) {
			sb.append("<h2>").append(resource.basePath).append("</h2>");
			sb.append("<ul>");

			for (MethodDescription method : resource.calls) {
				sb.append("<li> ").append(method.method).append(" ");
				sb.append("<strong>").append(method.fullPath)
						.append("</strong>");
				sb.append("<ul>");
				sb.append("<li>").append("CleanPath: ")
						.append(method.cleanPath).append("</li>");
				sb.append("</ul>");

				sb.append("<ul>");

				if (method.consumes != null) {
					sb.append("<li>").append("Consumes: ")
							.append(method.consumes).append("</li>");
				}

				if (method.produces != null) {
					sb.append("<li>").append("Produces: ")
							.append(method.produces).append("</li>");
				}

				sb.append("<li>").append("Parameter: ").append("</li>");
				sb.append("<ul>");
				for (ParamDescription p : method.paramDescriptions) {
					sb.append("<li>").append("paramType: ").append(p.paramType)
							.append("</li>");
					sb.append("<li>").append("variableType: ")
							.append(p.variableType).append("</li>");
					sb.append("<li>").append("name: ").append(p.name)
							.append("</li>");
				}
				sb.append("</ul>");

				sb.append("</ul>");
			}
			sb.append("</ul>");
		}
		return Response.ok(sb.toString()).build();

	}

	@GET
	@Path(value = "/update")
	@Produces(MediaType.TEXT_HTML)
	public Response updateToTable(@Context Dispatcher dispatcher) {

		StringBuilder sb = new StringBuilder();
		ResourceMethodRegistry registry = (ResourceMethodRegistry) dispatcher
				.getRegistry();
		List<ResourceDescription> descriptions = ResourceDescription
				.fromBoundResourceInvokers(registry.getBounded()
						.entrySet());

		int numInsert = 0;
		int numServices = 0;
		for (ResourceDescription resource : descriptions) {
			for (MethodDescription method : resource.calls) {
				List<Aksi> aksiList = aksisession
						.getAksiListByPath(method.cleanPath);
				if (aksiList.size() == 0) {
					Aksi a = new Aksi();

					a.setPath(method.cleanPath);

					aksisession.insertAksi(a, null);
					numInsert++;
				}
				numServices++;
			}
		}
		sb.append("Updating to table T1_AKSI<br/><br/>");
		sb.append("<br/>Services count    : " + numServices );
		sb.append("<br/>Inserted to table : " + numInsert + " rows" );
		return Response.ok(sb.toString()).build();

	}
}
