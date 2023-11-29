var app = angular.module('myApp', []);



/**
 * TODO: Use polling to continuously fetch the status of orders.
 */
app.controller('CustomerController', function($scope, $http, $q) {
	// Initial orders data for testing
	$scope.orders = [{id:1525, status:"In Progress", placedAt: "11:23 AM October 28, 2023"}, {id:5252, status:"Ready for pickup", placedAt: "11:23 AM October 28, 2023"}];
	
	$scope.onOrderPickup = function(order) {
		// For testing, removes a order from pickup
		$scope.orders = $scope.orders.filter(anOrder => anOrder !== order);
		
		// Now make the API call to pick it up and move it from state to state
		//$http.put("api/v1/orders/pickup").then(function(response){});
	}
  
    // Function to fetch orders from the server
    $scope.fetchOrders = function() {
		var name = sessionStorage.getItem("username");
        $http.get("/api/v1/orders/" + name).then(function(response) {
            // Update orders data with the fetched data
            $scope.orders = response.data;
            
            //Update the order status to be prettier
            $scope.orders.forEach(order=>{
				if(order.status === "UNFULFILLED"){
					order.status = "In Progress";
				}
				else{
					order.status = "Ready To Pickup"
				}
			});
				
        });
    }
  

    // Function to handle order pickup
    $scope.pickupOrder = function(order) {
        // Initialize success and failure flags
        $scope.success = false;
        $scope.failure = false;

		//var id = order.id;

        // Send a PUT request to the server to pick up the order by its id
        $http.put("/api/v1/orders/pickup", order.id).then(function(response){
            // On success, set the success flag to true
            $scope.success = true;
            // Log a success message
            console.log(response.data);
            console.log("Successfully picked up " + order.name);
            $scope.fetchOrders();
        }, function(rejection) {
            // On failure, set the failure flag to true
            console.error("Error while picking up coffee.");
            console.log(order.id);
            $scope.failure = true;
            // Set the error message based on the rejection data or a default message
            $scope.error = rejection.data && rejection.data.message ? rejection.data.message : "Unknown error";
            // Log the entire rejection object for debugging purposes
            console.log("Rejection object:", rejection);
        });
        $scope.fetchOrders();
    }


    // Fetch orders when the controller is initialized
    $scope.fetchOrders();
});