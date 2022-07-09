package id.co.promise.procurement.utils.audit;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;


public class ReflectionUtil {

	private static Object object;
	public static ReflectionUtil instance = new ReflectionUtil();
	
	 
	
	public static ReflectionUtil getInstance(Object objectBean) {
	    object = objectBean;
		return instance;
	}
	
	public Object getObject() {
		return object;
	}
	
	public String getFieldDataType(String fieldName) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
			return field.getType().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
	public Object getFieldValue(String fieldName) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setString(String fieldName, String value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setInteger(String fieldName, Integer value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDouble(String fieldName, Double value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setDate(String fieldName, Date value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLong(String fieldName, Long value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTimestamp(String fieldName, Timestamp value) {
		try {
			Class<?> clazz = object.getClass();
			Field field = clazz.getDeclaredField(fieldName);
		    field.setAccessible(true);
		    
			field.set(object,value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkField() {
		try {
			for (Field field : object.getClass().getDeclaredFields()) {
			    field.setAccessible(true); // You might want to set modifier to public first.
			    Object value = field.get(object); 
				    
			}
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		}
	}
}
