var app = angular.module('myApp', []);


app.controller('BaristaController', function($scope, $http, $q) {
		// Initialize scope variables with dummy data
    $scope.orders = [
        {id: 1525, status: "UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Coffee"},
        {id: 5252, status: "UNFULFILLED", placedAt: "11:23 AM October 28, 2023", recipe: "Latte"}
    ];
    $scope.error = null; // Displays the error for when fulfilling orders fails

    // Function to fetch unfulfilled orders from the server
    $scope.fetchOrders = function() {
        $http.get("/api/v1/orders/unfulfilled").then(function(response) {
            // Update orders data with the fetched data
            $scope.orders = response.data;
        });
    }

    // Function to fulfill an order
    $scope.fulfillOrder = function(order) {
        console.log(order);

        // Remove the order from the local array (used for testing)
        $scope.orders = $scope.orders.filter(anOrder => anOrder !== order);

        // Send a PUT request to mark the order as fulfilled
        $http.put("/api/v1/orders/fulfill", { id: order.id }).then(function(response) {
            // Log a success message
            console.log("Order with ID " + order.id + " fulfilled successfully.");
        }).catch(function(error) {
            // Log an error message if fulfilling the order fails
            console.error("Error fulfilling order with ID " + order.id, error);
            // Set the error variable for display on the UI
            $scope.error = "Error fulfilling order. Please try again.";
        }).finally(function() {
            // Reload current orders after attempting to fulfill an order
            $scope.fetchOrders();
        });
    }

    // Fetch unfulfilled orders when the controller is initialized
    $scope.fetchOrders();
});