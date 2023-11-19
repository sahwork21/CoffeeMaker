var app = angular.module('myApp', []);


app.controller('OrderController', function($scope, $http, $q) {
	$scope.hidePopup = true;
	$scope.selectedRecipe = null;
	$scope.payment = null;
	$scope.recipes = [];
	
	$scope.fetchRecipes = function() {
		$http.get("/api/v1/recipes").then(function(response) {
			$scope.recipes = response.data;			
		});
	}
	
	$scope.onRecipeSelect = function(recipe) {
		$scope.selectedRecipe = recipe;
		$scope.hidePopup = false;
	}
	
	$scope.cancelPopup = function() {
		$scope.hidePopup = true;
	}
	
	
	$scope.orderRecipe = function() {
		$scope.success = false;
		$scope.failure = false;
		
		// Create an object to pass as the request body
    var orderData = {
        username: sessionStorage.getItem("username"),
        recipeName: $scope.selectedRecipe.name,
        payment: $scope.payment
    };
    console.log(orderData);
		$http.post("/api/v1/orders", orderData).then(function(response) {
		    $scope.change = response.data.message;
		    $scope.success = true;
		    console.log("Successfully ordered " + $scope.selectedRecipe.name);
		}, function(rejection) {
		    console.error("Error while ordering coffee.");
		    $scope.failure = true;
		    $scope.error = rejection.data && rejection.data.message ? rejection.data.message : "Unknown error";
		    console.log("Rejection object:", rejection); // Log the entire rejection object
		});

		
	}
	
	$scope.fetchRecipes();
});