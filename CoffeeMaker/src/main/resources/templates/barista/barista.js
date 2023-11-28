var app = angular.module('myApp', []);


app.controller('BaristaController', function($scope, $http, $q) {
	$scope.orders = [{id:1525, status:"UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Coffee"}, {id:5252, status:"UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Latte"}];
	$scope.error = null; // Displays the error for when fulfilling orders fails
	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders/unfulfilled").then(function(response) {
			$scope.orders = response.data;
		});
	}
	
	$scope.fulfillOrder = function(order) {
		console.log(order);
		
		// This removes the order from the array. Used for testing
		$scope.orders = $scope.orders.filter(anOrder => anOrder !== order);
		
		// Send API request so we can find by id
		$http.put("/api/v1/orders/fulfill", order.id);
		
		// Reload current orders
		$scope.fetchOrders();
	}
	
	$scope.fetchOrders();
});