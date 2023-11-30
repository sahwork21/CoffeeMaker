var app = angular.module('myApp', []);

app.controller('ManageBaristasController', function($scope, $http, $q) {

    // Initialize scope variables
    $scope.accounts = []; // Stores the list of barista accounts
    $scope.formData = {username: "", password: "", roleType: 2}; // Stores the state of username, password, and role
    $scope.invalid = { username: false, password: false }; // Flags to highlight input as required/red

    // Function to reset the invalid flag when a textbox is clicked to edit
    $scope.resetInvalid = function(field) {
        $scope.invalid[field] = false;
    }

    // Error message to be displayed on the UI
    $scope.error = "";

    // Function to fetch barista accounts from the server
    $scope.fetchBaristaAccounts = function() {
        $http.get("/api/v1/users/barista").then(function(response) {
            // Update accounts data with the fetched data
            $scope.accounts = response.data;
            console.log(response.data);
        }).catch(function(err) {
            // Log any errors that occur during the fetch
            console.log(err);
        });
    }

    // Function to handle form submission
    $scope.onSubmit = function() {
        // Check for invalid username and password
        let invalidUsername = $scope.formData.username == "";
        let invalidPassword = $scope.formData.password == "";

        // Highlight invalid fields and return if any are invalid
        if (invalidUsername) $scope.invalid.username = true;
        if (invalidPassword) $scope.invalid.password = true;
        if (invalidUsername || invalidPassword) return;

        // Send a POST request to the server to create a new barista account
        $http.post("/api/v1/users/", $scope.formData).then(function(success) {
            // Reset error and form data on success
            $scope.error = "";
            $scope.formData.username = "";
            $scope.formData.password = "";
            $scope.invalid.password = false;
            console.log(success.data.message);

            // Fetch updated barista accounts after a new account is created
            $scope.fetchBaristaAccounts();
        }).catch(function(err) {
            // Highlight username and password as invalid on error
            $scope.invalid.username = true;
            $scope.invalid.password = true;
            // Set an error message for display on the UI
            $scope.error = "Invalid username or Password";
            console.error("Error: " + err.data.message);
        });
    }

    // Fetch barista accounts when the controller is initialized
    $scope.fetchBaristaAccounts();

});