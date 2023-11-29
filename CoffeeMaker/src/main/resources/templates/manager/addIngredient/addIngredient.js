var app = angular.module('myApp', []);

app.controller('AddIngredientController', function($scope, $http, $q) {

    // Ingredient object that is to be sent in PUT
    $scope.ingredient = {
        name: '',
        amount: '',
        isInventory: false
    }

    // Object to track invalid fields
    $scope.invalid = {};

    // Function to add a new ingredient
    $scope.addIngredient = function() {
        $scope.success = false;
        $scope.failure = false;

        // Send a PUT request to add the new ingredient to the inventory
        $http.put("/api/v1/inventory/ingredients", $scope.ingredient).then(
            function(response) {
                $scope.success = true;
                $scope.reset();
                console.log(response.data.message);
            }, function(rejection) {
                $scope.failure = rejection.data.message;
                $scope.success = false;
                console.error("Error while updating inventory: " + rejection.data.message);
            });
    }

    // Function used by the Input elements, when a textbox is clicked to edit, reset invalid.
    $scope.resetInvalid = function(field) {
        $scope.invalid[field] = false;
    }

    // VALIDATION

    // Generic validation function
    onValidate = function(field, condition) {
        $scope.invalid[field] = !condition;
        return condition;
    }

    // Validation for ingredient name
    $scope.validateName = function() {
        return onValidate('name', $scope.ingredient.name && $scope.ingredient.name.length > 0);
    };

    // Validation for ingredient amount
    $scope.validateAmount = function() {
        return onValidate('amount', $scope.ingredient.amount > 0);
    };

    // Validation for the entire ingredient
    $scope.validateIngredient = function() {
        isValid = true;
        if (!$scope.validateName()) isValid = false;
        if (!$scope.validateAmount()) isValid = false;
        return isValid;
    }

    // VALIDATION

    // Function to submit the form
    $scope.submit = function() {
        if (!$scope.validateIngredient()) return;
        $scope.addIngredient();
    }

    // Reset the form
    $scope.reset = function() {
        $scope.ingredient = {
            name: '',
            amount: '',
            isInventory: false
        };
    }

    $scope.reset();

});
