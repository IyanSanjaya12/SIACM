angular.module('naut')
    .directive('wrapOwlcarousel', ['$timeout', function ($timeout) {  
        return {
            restrict: 'E',  
            transclude: false,
            link: function (scope, element, attrs) {  
            	scope.initCarousel = function(element) {
            		$timeout(function () {
                        // provide any default options you want
                          var defaultOptions = {
                          };
                          var customOptions = scope.$eval($(element).attr('data-options'));
                          // combine the two options objects
                          for(var key in customOptions) {
                              defaultOptions[key] = customOptions[key];
                          }
                          // init carousel
                          $(element).owlCarousel(defaultOptions);
                     },100);
                  };
            }  
        };  
}])
.directive('wrapOwlcarouselItem', [function() {
    return {
        restrict: 'A',
        transclude: false,
        link: function(scope, element) {
          // wait for the last item in the ng-repeat then call init
            if(scope.$last) {
                scope.initCarousel(element.parent());
            }
        }
    };
}])