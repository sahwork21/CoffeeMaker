var app = angular.module('myApp', []);



/**
 * TODO: Use polling to continuously fetch the status of orders.
 */
app.controller('CustomerController', function($scope, $http, $q) {
	$scope.orders = [{id:1525, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}, {id:5252, status:"Ready for pickup", placedAt: "11:23 AM October 28, 2023"}];
	
	$scope.onOrderPickup = function(order) {
		// For testing, removes a order from pickup
		$scope.orders = $scope.orders.filter(anOrder => anOrder !== order);
	}
	
	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders").then(function(response) {
			$scope.orders = response.data;
		});
	}
	
	//$scope.fetchOrders();
	
});