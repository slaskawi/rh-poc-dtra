'use strict';

var ws = angular.module('myApp.services',[]);

ws.factory('Solutions', ['$resource', function($resource){
	return {
		list: function(callback) {
			var api = $resource("application/scheduler/solutions",{});

			api.query({}, function(response){
				callback(response);
			});
		}
	};
}]);

ws.factory('Tasks', ['$resource', function($resource){
	return {
		listByWPG: function(wpg, callback) {
			var api = $resource("application/scheduler/tasks/wpg/:wpg",{ wpg : wpg });

			api.query({}, function(response){
				callback(response);
			});
		}
	};
}]);

ws.factory('Engineers', ['$resource', function($resource){
	return {
		list: function(callback) {
			var api = $resource("application/scheduler/engineers",{});

			api.query({}, function(response){
				callback(response);
			});
		}
	};
}]);


var app = angular.module("myApp", ['ngCookies',
                                   'ngAnimate',
                                   'ngResource',
                                   'ngSanitize',
                                   'ngRoute',
                                   'ui.bootstrap',
                                   'ui.grid',
                                   'ui.grid.selection',
                                   'ui.grid.resizeColumns',
                                   'ui.grid.autoResize',
                                   'myApp.services']);

app.config(function($routeProvider){
    $routeProvider
	    .when("/", {
	        controller: "ctrlGeneric",
	        templateUrl: "pages/home.html"
	    })
	    .when("/scheduler", {
	        controller: "ctrlScheduler",
	        templateUrl: "pages/scheduler.html"
	    })
	    .when("/solutions", {
	        controller: "ctrlSolutions",
	        templateUrl: "pages/solutions.html"
	    })
	    .otherwise({
			redirectTo: '/'
		});
})

app.controller("ctrlMain", function($scope, $location)
{
	$scope.alerts = [];
	
	$scope.isActive = function (viewLocation) { 
        return viewLocation === $location.path();
    };	

    $scope.closeAlert = function(index) {
		$scope.alerts.splice(index, 1);
	};
});

app.controller("ctrlScheduler", function($scope, $resource)
{
	$scope.schedule = function (){
		var api = $resource("application/scheduler/solve",{});

		api.save( { engineerList : $scope.solution.engineers, taskList : $scope.solution.tasks }, function(response){
			$scope.alerts.push(response);
		});
	};
	
	$scope.solution = { tasks : [], engineers : [] };
	
});

app.controller("ctrlSolutions", function($scope, $resource, $interval, Solutions)
{
	$scope.tblData = [{id:39890558, hardScore : 0, softScore : -15, feasible : true}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'Solution', maxWidth: 100 },
	                     { name: 'score.hardScore', displayName: 'Hard' },
	                     { name: 'score.softScore', displayName: 'Soft' },
	                     { name: 'score.feasible', displayName: 'Feasible'},
	                     { name: 'timestamp', displayName: 'Created On'}];

	$scope.gridOptionsSolutions = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			selectedItems: $scope.tblSelections,
			showGridFooter: true,
			multiSelect: false };
	
	var stop = $interval(function() {
		Solutions.list(function(data){
			$scope.gridOptionsSolutions.data = data;
		});		
	}, 1000);
	
	$scope.$on('$destroy', function() {
        $scope.stop();
    });
	
	$scope.stop = function() {
        if (angular.isDefined(stop)) {
          $interval.cancel(stop);
          stop = undefined;
        }
    };
	
	$scope.terminate = function (){
		var api = $resource("application/scheduler/terminateEarly",{});

		api.save({}, function(response){
			$scope.alerts.push(response);
		});
	};
});

app.controller("ctrlEngineers", function($scope, Engineers)
{
	$scope.wpg = '7471264-2-M102';
	
	$scope.tblData = [{id:39890558}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 100 },
	                     { name: 'actname', displayName: 'Name', minWidth: 300 },
	                     { name: 'duration', displayName: 'Duration', maxWidth: 85, cellFilter: 'date : \'HH:mm:ss\' : \'GMT\'' },
	                     //{ name: 'priority', displayName: 'Priority' },
	                     { name: 'earlyStart', displayName: 'Early Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\''},
	                     { name: 'lateStart', displayName: 'Late Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'dueDate', displayName: 'Due Date', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'workpackagegroup', displayName: 'WPG' }];

	$scope.gridOptionsEng = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			selectedItems: $scope.tblSelections,
			showGridFooter: true,
			multiSelect: false };
	
	Engineers.list(function(data) {
		$scope.solution.engineers = data;
		$scope.gridOptionsEng.data = $scope.solution.engineers;
	});
});

app.controller("ctrlTasks", function($scope, Tasks)
{
	$scope.wpg = '7471264-2-M102';
	
	$scope.tblData = [{w6key:39890558, actname:'Upload and processing FMT File in TIPP tool', duration:2376000}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 80 },
	                     { name: 'actname', displayName: 'Name', minWidth: 300 },
	                     { name: 'duration', displayName: 'Duration', maxWidth: 85, cellFilter: 'date : \'HH:mm:ss\' : \'GMT\'' },
	                     //{ name: 'priority', displayName: 'Priority' },
	                     { name: 'earlyStart', displayName: 'Early Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\''},
	                     { name: 'lateStart', displayName: 'Late Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'dueDate', displayName: 'Due Date', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'workpackagegroup', displayName: 'WPG' }];

	$scope.gridOptionsTasks = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			selectedItems: $scope.tblSelections,
			showGridFooter: true,
			multiSelect: false };

	$scope.loadWPG = function() {
		Tasks.listByWPG($scope.wpg, function(data) {
			$scope.solution.tasks = data;
			$scope.gridOptionsTasks.data = $scope.solution.tasks;
		});
    };
    
    $scope.loadWPG();
});

app.controller("ctrlGeneric", function($scope)
{
	
});