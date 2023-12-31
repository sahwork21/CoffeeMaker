var app = angular.module('myApp', []);


app.controller('BaristaController', function($scope, $http, $q) {
	$scope.orders = [{id:1525, status:"UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Coffee"}, {id:5252, status:"UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Latte"}];
	$scope.error = null; // Displays the error for when fulfilling orders fails
	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders/unfulfilled").then(function(response) {
			$scope.orders = response.data;
			//Update the order status to be prettier
            $scope.orders.forEach(order=>{
				order.status = 'In Progress';
			});
		});
	}
	
	$scope.fulfillOrder = function(order) {
		console.log(order);
		
		// This removes the order from the array. Used for testing
		//$scope.orders = $scope.orders.filter(anOrder => anOrder !== order);
		
		// Send API request so we can find by id
		// Make sure that we can actually make this recipe first
		$http.post("/api/v1/makecoffee/" + order.recipe, order.payment).then(
			function(response) {
					$scope.change = response.data.message;
					$scope.submissionSuccess = true;
					
					// Now we know that making is possible fulfill the order
					$http.put("/api/v1/orders/fulfill", order.id).then(function(success){
						// Reload current orders
						$scope.fetchOrders();
					}, function(rejection){
						$scope.submissionFailure = true;
						$scope.error = "Error while making recipe";
						// Somebody else already fulfilled it so nothing should happen
					});
					
				}, function(errResponse) {
					console.log(errResponse);
					$scope.submissionFailure = true;
					$scope.error = "Error while making recipe. Not enough ingredients.";
						
		});
		
		// Reload current orders
		//$scope.fetchOrders();
	}
	
	$scope.fetchOrders();
	
	setInterval($scope.fetchOrders, 500);
});