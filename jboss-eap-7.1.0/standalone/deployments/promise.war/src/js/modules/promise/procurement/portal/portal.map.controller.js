
(function() {
	'use strict';

	angular.module('naut').controller('PortalMapController',
			PortalMapController);
	
	function PortalMapController($scope, $timeout) {

		

		var item = {
			/*coordinates : [ -6.2334099, 106.8507507 ]*/
				coordinates : [-6.107744, 106.883141] 
		};

		var woa = {
			city : 'This is my marker. There are many like it but this one is mine.'
		};

		$scope.mymarker = new google.maps.Marker({
			map : $scope.myMap,
			animation : google.maps.Animation.DROP,
			position : new google.maps.LatLng(item.coordinates[0],item.coordinates[1]),
			title : woa.city
		});

		$scope.myMarkers = [
		new google.maps.Marker({
			map : $scope.myMap,
			animation : google.maps.Animation.DROP,
			position : new google.maps.LatLng(item.coordinates[0],item.coordinates[1]),
			title : woa.city})];
		
			
			
			
		$scope.mapOptions = {
			center : new google.maps.LatLng(40.0000, -98.0000),
			zoom : 15,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		$scope.addMarker = function($event, $params) {
			$scope.myMarkers.push(new google.maps.Marker({
				map : $scope.myMap,
				position : $params[0].latLng
			}));
		};

		$scope.setZoomMessage = function(zoom) {
			$scope.zoomMessage = 'You just zoomed to ' + zoom + '!';
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

			$timeout(function() {
				google.maps.event.trigger($scope.myMap, 'resize');
			}, 100);

		};

	}
	PortalMapController.$inject = [ '$scope', '$timeout' ];
})();
