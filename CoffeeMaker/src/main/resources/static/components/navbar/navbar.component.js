

function NavBarController($scope, $element, $attrs) {
	var ctrl = this;
	ctrl.username = sessionStorage.getItem("username");
	ctrl.userRole = sessionStorage.getItem("userRole");
	console.log(ctrl.userRole)
	
	if (ctrl.userRole == null) {
		ctrl.username = ""
	}
	
	
	ctrl.signOut = function() {
		sessionStorage.setItem("username", null);
		sessionStorage.setItem("userRole", null);
		location.href = "signin.html";
	}
	
	ctrl.goToMain = function() {
		redirects = {
			"CUSTOMER": "customer.html",
			"MANAGER": "manager.html",
			"BARISTA": "barista.html"
		}
		
		console.log(redirects[ctrl.userRole])
		
		if (redirects[ctrl.userRole]) {
			location.href = "../../" + redirects[ctrl.userRole]
		} else {
			location.href = "signin.html"
		}
		
	}
}

angular.module('myApp').component('navbar', {
	templateUrl: '/components/navbar/navbar.template.html',
	controller: NavBarController
});

