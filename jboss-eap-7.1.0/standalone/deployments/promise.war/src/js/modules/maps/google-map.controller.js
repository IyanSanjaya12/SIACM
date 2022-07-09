/**=========================================================
 * Module: GoogleMapController.js
 * Google Map plugin controller
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('naut')
        .controller('GoogleMapController', GoogleMapController);
    
    function GoogleMapController($scope, $timeout) {
     
      $scope.myMarkers = [];
     
      $scope.mapOptions = {
        center: new google.maps.LatLng(35.784, -78.670),
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
       
      $scope.addMarker = function($event, $params) {
        $scope.myMarkers.push(new google.maps.Marker({
          map: $scope.myMap,
          position: $params[0].latLng
        }));
      };
       
      $scope.setZoomMessage = function(zoom) {
        $scope.zoomMessage = 'You just zoomed to '+zoom+'!';
      };
       
      $scope.openMarkerInfo = function(marker) {
        $scope.currentMarker = marker;
        $scope.currentMarkerLat = marker.getPosition().lat();
        $scope.currentMarkerLng = marker.getPosition().lng();
        $scope.myInfoWindow.open($scope.myMap, marker);
      };
       
      $scope.setMarkerPosition = function(marker, lat, lng) {
        marker.setPosition(new google.maps.LatLng(lat, lng));
      };

      $scope.refreshMap = function() {
        
        $timeout(function(){
          google.maps.event.trigger($scope.myMap, 'resize');
        }, 100);

      };

    }
    GoogleMapController.$inject = ['$scope', '$timeout'];
})();
