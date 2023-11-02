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
		// Check valid payment.
		$scope.hidePopup = true;
		// Send order request.
	}
	
	$scope.fetchRecipes();
});