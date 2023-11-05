var app = angular.module('myApp', []);


app.controller('ManagerController', function($scope, $http, $q) {
	$scope.actions = [
		{title: "Add inventory", description: "Running low?", "icon": ("<svg></svg>")},
		{title: "Add Recipe", description: "Have a creative idea?", "icon": null}
	]
	$scope.orders = [{id:1525, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}, {id:5252, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}];
	
	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders").then(function(response) {
			$scope.orders = response.data;
		});
	}
	
	//$scope.fetchOrders();
});