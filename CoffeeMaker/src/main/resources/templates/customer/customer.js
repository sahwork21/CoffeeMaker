var app = angular.module('myApp', []);


app.controller('CustomerController', function($scope, $http, $q) {
	// Initial orders data for testing
    $scope.orders = [{id:1525, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}, {id:5252, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}];

    // Function to fetch orders from the server
    $scope.fetchOrders = function() {
        $http.get("/api/v1/orders").then(function(response) {
            // Update orders data with the fetched data
            $scope.orders = response.data;
        });
    }

    // Function to handle order pickup
    $scope.pickupOrder = function(order) {
        // Initialize success and failure flags
        $scope.success = false;
        $scope.failure = false;

        // Send a PUT request to the server to pick up the order
        $http.put("/api/vi/orders/pickup", order).then(function(response){
            // On success, set the success flag to true
            $scope.success = true;
            // Log a success message
            console.log("Successfully picked up " + order.name);
        }, function(rejection) {
            // On failure, set the failure flag to true
            console.error("Error while picking up coffee.");
            $scope.failure = true;
            // Set the error message based on the rejection data or a default message
            $scope.error = rejection.data && rejection.data.message ? rejection.data.message : "Unknown error";
            // Log the entire rejection object for debugging purposes
            console.log("Rejection object:", rejection);
        });
    }

    // Fetch orders when the controller is initialized
    $scope.fetchOrders();
});