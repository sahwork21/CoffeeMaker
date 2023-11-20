var app = angular.module('myApp', []);

app.controller('OrderController', function($scope, $http, $q) {


    // Initialize scope variables
    $scope.hidePopup = true;
    $scope.selectedRecipe = null;
    $scope.payment = null;
    $scope.recipes = [];

    // Function to fetch recipes from the server
    $scope.fetchRecipes = function() {
        $http.get("/api/v1/recipes").then(function(response) {
            // Update recipes data with the fetched data
            $scope.recipes = response.data;
        });
    }

    // Function to handle recipe selection
    $scope.onRecipeSelect = function(recipe) {
        // Update selectedRecipe and show the popup
        $scope.selectedRecipe = recipe;
        $scope.hidePopup = false;
    }

    // Function to cancel the popup
    $scope.cancelPopup = function() {
        // Hide the popup
        $scope.hidePopup = true;
    }

    // Function to order the selected recipe
    $scope.orderRecipe = function() {
        // Initialize success and failure flags
        $scope.success = false;
        $scope.failure = false;

        // Create an object to pass as the request body
        var orderData = {
            username: sessionStorage.getItem("username"),
            recipeName: $scope.selectedRecipe.name,
            payment: $scope.payment
        };
        // Log the order data for debugging purposes
        console.log(orderData);

        // Send a POST request to the server to place an order
        $http.post("/api/v1/orders", orderData).then(function(success) {
            // Update scope variables on success
            $scope.change = success.data.message;
            $scope.success = true;
            // Log a success message
            console.log("Successfully ordered " + $scope.selectedRecipe.name);
        }, function(rejection) {
            // On failure, log an error message and update scope variables
            console.error("Error while ordering coffee.");
            $scope.failure = true;
            $scope.error = rejection.data && rejection.data.message ? rejection.data.message : "Unknown error";
            // Log the entire rejection object for debugging purposes
            console.log("Rejection object:", rejection);
        });
    }

    // Fetch recipes when the controller is initialized
    $scope.fetchRecipes();
});

