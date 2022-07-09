/*
 * GRUNT Script 
 * Author : Reinhard
 */

module.exports = function(grunt) {
	'use strict';
//	var buildPath = 'G://KAI/KAI/build_grunt';
	var buildPath = '/opt/promise/promise_build';

	/* DO NOT MODIFY */
	var warFileName = 'promise.war';
	var srcPath = 'src/main/webapp';
	var combinedCssFileName = 'app/css/promise.css';
	var combinedJsFileName = 'src/js/modules/promise/promise.js';
	var combinedCssFileNameAndBuildPath = buildPath + '/' + combinedCssFileName;
	var combinedJsFileNameAndBuildPath = buildPath + '/' + combinedJsFileName;
	var appVersion = '?v=<%=appVersion%>';
	
	grunt.log.writeln('== ProMISE Builder ==');

	grunt.initConfig({

		pkg : grunt.file.readJSON('package.json'),

		jsResources : [],
		cssResources : [],
		vendorResources : [],

		clean : {
			build : {
				src : [ buildPath ]
			},
		},

		copy : {
			build : {
				cwd : srcPath,
				src : [ '**' ],
				dest : buildPath,
				expand : true
			},
		},

		replace : {
			gather : {
				files : [ {
					cwd : buildPath,
					dest : buildPath,
					expand : true,
					src : [ 'index.jsp' ]
				} ],
				options : {
					patterns : [ {

						match : /\<\!\-\-build\-js\-start[\s\S]*build\-js\-end\-\-\>/,
						replacement : function(matchedString) {
							var jsArray = matchedString.match(/(src\s?\=\s?[\'\"])([^\'\"]*)([\'\"])/g);
							jsArray.forEach(function(element) {
								var resourceTarget = element.match(/(src\s?\=\s?[\'\"])([^\'\"]*)([\'\"])/)[2];
								var targetConfig = grunt.config('jsResources');
								grunt.log.writeln(resourceTarget);
								targetConfig.push(buildPath + '/' + resourceTarget);
								grunt.config('jsResources', targetConfig);
							});
							return '<script type="text/javascript" src="' + combinedJsFileName + appVersion + '"></script>';
						}
					}, {

						match : /\<\!\-\-build\-css\-start[\s\S]*build\-css\-end\-\-\>/,
						replacement : function(matchedString) {
							var cssArray = matchedString.match(/(href\s?\=\s?[\'\"])([^\'\"]*)([\'\"])/g);
							cssArray.forEach(function(element) {
								var resourceTarget = element.match(/(href\s?\=\s?[\'\"])([^\'\"]*)([\'\"])/)[2];
								var targetConfig = grunt.config('cssResources');
								targetConfig.push(buildPath + '/' + resourceTarget);
								grunt.config('cssResources', targetConfig);
							});

							return '<link rel="stylesheet" media="screen" href="' + combinedCssFileName + appVersion + '"/>';
						}
					}]
				}
			}
		},

		concat : {
			js : {

				src : [ '<%= jsResources %>' ],
				dest : combinedJsFileNameAndBuildPath,
				options : {
					separator : ';\n'
				}
				
			},
			css : {

				src : [ '<%= cssResources %>' ],
				dest : combinedCssFileNameAndBuildPath
			}
		},

		ngAnnotate : {
			options : {
				singleQuotes : true
			},
			app : {
				files : {
					combinedJsFileNameAndBuildPath : [ combinedJsFileNameAndBuildPath ]
				}
			}
		},
		
		uglify : {
			options : {
				mangle : false 
			},
			js : {
				src : [ combinedJsFileNameAndBuildPath ],
				dest : combinedJsFileNameAndBuildPath
			}
		},

		cssmin : {
			css: {
		        src: combinedCssFileNameAndBuildPath,
		        dest:combinedCssFileNameAndBuildPath
		      }
		},

		war : {
			target : {
				options : {
					war_dist_folder : buildPath,
					war_name : warFileName
				},
				files : [ {
					expand : true,
					cwd : buildPath,
					src : [ '**' ],
					dest : ''
				} ]
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-ng-annotate');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-replace');
	grunt.loadNpmTasks('grunt-war');

	grunt.registerTask('build', 'Creates a build from the files in the app directory', function() {
		grunt.option('force', true);
		grunt.log.writeln('Starting...');
		grunt.task.run([ 'clean', 'copy', 'replace:gather', 'concat:css', 'cssmin', 'concat:js', 'ngAnnotate:app', 'uglify:js', 'war' ]);
	});

};