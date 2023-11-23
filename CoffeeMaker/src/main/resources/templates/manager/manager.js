var app = angular.module('myApp', []);


app.controller('ManagerController', function($scope, $http, $q) {
	$scope.actions = [
		{ title: "Add inventory", description: "Running low?", "icon": ("<svg></svg>"), href: "/inventory", color: "text-blue-500" },
		{ title: "Add Recipe", description: "Have a creative idea?", "icon": null, href: "/addRecipe", color: "text-amber-500" },
		{ title: "Edit Recipe", description: "Have a creative idea?", "icon": null, href: "/editrecipe", color: "text-indigo-500" },
		{ title: "Delete Recipe", description: "Delete an existing recipe", "icon": null, href: "/deleteRecipe", color: "text-green-700" },
		{ title: "Add Ingredient", description: "Pumpkin spice season", "icon": null, href: "/addIngredient", color: "text-yellow-500" },
		{ title: "Manage Baristas", description: "Add new member", "icon": null, href: "/manageBaristas", color: "text-stone-500" },
	]
	
	$scope.ordersData = [];

	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders").then(function(response) {
			$scope.orders = response.data;
		});
	}

	//$scope.fetchOrders();
});