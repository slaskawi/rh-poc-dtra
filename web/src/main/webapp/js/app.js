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
	    .when("/rules", {
	        controller: "ctrlRules",
	        templateUrl: "pages/rules.html"
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

app.controller("ctrlSolutions", function($scope, $resource, $interval, $log, Solutions)
{
	$scope.solution = {id:{}, score : {}, engineerList: [], tasklist : []};
	
	$scope.tblData = [{id:39890558, hardScore : 0, softScore : -15, feasible : true}];
	$scope.tblColumns = [{ name: 'id', displayName: 'Solution', maxWidth: 120 },
	                     { name: 'score.hardScore', displayName: 'Hard' },
	                     { name: 'score.softScore', displayName: 'Soft' },
	                     { name: 'score.feasible', displayName: 'Feasible'},
	                     { name: 'timestamp', displayName: 'Created On', cellFilter: 'date : \'yyyy-MM-dd HH:mm:ss.sss\''}];

	$scope.gridOptionsSolutions = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			showGridFooter: true,
			enableRowSelection: false,
			enableFullRowSelection: true,
			multiSelect: false };
	
    $scope.gridOptionsSolutions.onRegisterApi = function(gridApi){
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope,function(row){
          var msg = 'row selected ' + row.isSelected;
          $log.log(msg);
          $scope.solution = row.entity;
        });
   
        gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
          var msg = 'rows changed ' + rows.length;
          $log.log(msg);
        });
      };
	
	var stopInterval = $interval(function() {
		Solutions.list(function(data){
			$scope.gridOptionsSolutions.data = data;
		});		
	}, 1000);
	
	$scope.$on('$destroy', function() {
        $scope.stop();
    });
	
	$scope.stop = function() {
        if (angular.isDefined(stopInterval)) {
          $interval.cancel(stopInterval);
          stopInterval = undefined;
        }
    };
	
	$scope.terminate = function (){
		var api = $resource("application/scheduler/terminateEarly",{});

		api.save({}, function(response){
			$scope.stop();
			$scope.alerts.push(response);
		});
	};
	
	$scope.nextTasks = function(next, tasks) {
		if (next === undefined || next === null) {
			return tasks;
		}
		tasks.push(next);
		return $scope.nextTasks(next.nextTask, tasks);
	}
});

app.controller("ctrlEngineers", function($scope, $filter, Engineers)
{
	$scope.tblData = [{id:39890558}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 100 },
	                     { name: 'gsc', displayName: 'GSC' },
	                     { name: 'unit', displayName: 'Unit' },
	                     { name: 'subunit', displayName: 'Subunit' },
	                     { name: 'region', displayName: 'Region' },
	                     { name: 'disctrict', displayName: 'District' }];

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
	
	$scope.isViewDay = true;
	$scope.dateAuto = new Date();
	
	$scope.today= function()
	{
		$scope.dateAuto = new Date();
		if($scope.isViewDay)
		{
			$scope.viewActivitieDay();
		}else
			{
				$scope.viewActivitieWeek();
			}
	}
	$scope.before = function()
	{
		if($scope.isViewDay)
		{
			var previousDay = -1;
			$scope.dateAuto.setDate($scope.dateAuto.getDate() + previousDay);
			$scope.viewActivitieDay();
		}else
			{
				var previousDay = -7;
				$scope.dateAuto.setDate($scope.dateAuto.getDate() + previousDay);
				$scope.viewActivitieWeek();
			}
		
	}
	$scope.after = function()
	{
		if($scope.isViewDay)
		{
			var nextDay = 1;
			$scope.dateAuto.setDate($scope.dateAuto.getDate() + nextDay);
			$scope.viewActivitieDay();
		}else
			{
			var nextDay = 7;
			$scope.dateAuto.setDate($scope.dateAuto.getDate() + nextDay);
			$scope.viewActivitieWeek();
			}
	}
	
	$scope.viewActivitieDay= function()
	{
		$scope.isViewDay = true;
		$scope.tblData = [{id: 'id1', enginner : 'LUIS' + $scope.dateAuto, activitie  : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
		     			{id: 'id2', enginner : 'LUIS2',activitie : 'ACIVITIE_2', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
		     			{id: 'id2',enginner : 'LUIS3',activitie : 'ACIVITIE_3', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""}];
		$scope.tblSelections = [];
		$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 80 },
		                     { name: 'enginner', displayName: 'Name Enginnner', minWidth: 300 },
		                     { name: 'activitie', displayName: 'Task', maxWidth: 85},
		                     { name: 'op1',  displayName: '09:00'},
     			             { name: 'op2', displayName: '10:00'},
     			             { name: 'op3', displayName: '11:00'},
     			             { name: 'op4', displayName: '12:00'},
     			             { name: 'op5', displayName: '13:00'},
     			             { name: 'op6', displayName: '14:00'},
     			             { name: 'op7', displayName: '15:00'},
     			             { name: 'op8', displayName: '16:00'},
     			             { name: 'op9', displayName: '17:00'},
     			             { name: 'op10', displayName: '18:00'}];
		$scope.gridOptionsEng = { 
				data: $scope.tblData, 
				columnDefs: $scope.tblColumns,
				selectedItems: $scope.tblSelections,
				showGridFooter: true,
				multiSelect: false };
	}
	$scope.viewActivitieWeek= function()
	{
		$scope.isViewDay = false;
		
		$scope.startWeek;
		$scope.startDay;
		
		$scope.dayWeek = $filter('date')(new Date($scope.dateAuto), 'EEEE');
		$scope.day = $filter('date')(new Date($scope.dateAuto), 'dd');
		
		if($scope.dayWeek == 'Monday')
		{
			$scope.$startWeek = $scope.dayWeek;  
			$scope.startDay = $scope.day; 
		}else
			if($scope.dayWeek == 'Tuesday')
			{
				$scope.$startWeek = 'Monday';
				$scope.day--;//-1 day
				$scope.startDay = $scope.day; 
			}
			else
				if($scope.dayWeek == 'Wednesday')
				{
					$scope.$startWeek = 'Monday';
					$scope.day--;$scope.day--;//-2 day
					$scope.startDay = $scope.day; 
				}
				else
					if($scope.dayWeek == 'Thursday')
					{
						$scope.$startWeek = 'Monday';
						$scope.day--;$scope.day--;$scope.day--;//-3 day
						$scope.startDay = $scope.day; 
					}
					else
						if($scope.dayWeek == 'Friday')
						{
							$scope.$startWeek = 'Monday';
							$scope.day--;$scope.day--;$scope.day--;$scope.day--;//-4 day
							$scope.startDay = $scope.day; 
						}
						else
							if($scope.dayWeek == 'Saturday')
							{
								$scope.$startWeek = 'Monday';
								$scope.day--;$scope.day--;$scope.day--;$scope.day--;$scope.day--;//-5 day
								$scope.startDay = $scope.day; 
							}
							else
								if($scope.dayWeek == 'Sunday')
								{
									$scope.$startWeek = 'Monday';
									$scope.day--;$scope.day--;$scope.day--;$scope.day--;$scope.day--;$scope.day--;//-6 day
									$scope.startDay = $scope.day; 
								}
		
		$scope.week = $filter('date')(new Date($scope.dateAuto), 'w');
		
		$scope.dayIncrement = $scope.startDay;
		$scope.dayIncrement++;
		$scope.twoMoreDay = $scope.dayIncrement;
		$scope.dayIncrement++;
		$scope.threeMoreDay = $scope.dayIncrement;
		$scope.dayIncrement++;
		$scope.fourMoreDay = $scope.dayIncrement;
		$scope.dayIncrement++;
		$scope.fiveMoreDay = $scope.dayIncrement;
		$scope.dayIncrement++;
		$scope.sixMoreDay = $scope.dayIncrement;
		$scope.dayIncrement++;
		$scope.sevenMoreDay = $scope.dayIncrement;
		
		$scope.tblData = [{day1 : "LUIS ACIVITIE_1", day2 : $scope.dateAuto, day3 : "", day4 : "", day5 : "", day6 : "", day7 : ""},
		     			{day1 : "", day2 : "", day3 : "LUIS2 ACIVITIE_2", day4 : "", day5 : "", day6 : "", day7 : ""},
		     			{day1 : "", day2 : "", day3 : "", day4 : "", day5 : "LUIS3 ACIVITIE_3", day6 : "", day7 : ""}];
		$scope.tblSelections = [];
		$scope.tblColumns = [{ name: 'day1',  displayName: $scope.$startWeek + ' ' + $scope.week +'/'+ $scope.startDay},
		                     { name: 'day2',  displayName: 'Tuesday' + ' ' + $scope.week +'/'+ $scope.twoMoreDay},
		                     { name: 'day3',  displayName: 'Wednesday' + ' ' + $scope.week +'/'+ $scope.threeMoreDay},
							 { name: 'day4',  displayName: 'Thursday' + ' ' + $scope.week +'/'+ $scope.fourMoreDay},
							 { name: 'day5',  displayName: 'Friday' + ' ' + $scope.week +'/'+ $scope.fiveMoreDay},
							 { name: 'day6',  displayName: 'Saturday' + ' ' + $scope.week +'/'+ $scope.sixMoreDay},
							 { name: 'day7',  displayName: 'Sunday' + ' ' + $scope.week +'/'+ $scope.sevenMoreDay}];
		$scope.gridOptionsEng = { 
				data: $scope.tblData, 
				columnDefs: $scope.tblColumns,
				selectedItems: $scope.tblSelections,
				showGridFooter: true,
				multiSelect: false };
	}
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

app.controller("ctrlRules", function($scope)
		{
			$scope.sequence = 12355;
			$scope.listRules = [{idRule:12345, name:'Base priority revenue', weight: '500'},
			                    {idRule:12346, name:'Consider Preferred Engineers', weight: '400'},
			                    {idRule:12347, name:'Minimize Resource Idle Time', weight: '25'},
			                    {idRule:12348, name:'Prefer appointments', weight: '200'},
			                    {idRule:12349, name:'Priority by appointment finish', weight: '400'},
			                    {idRule:12350, name:'Priority by due date', weight: '400'},
			                    {idRule:12351, name:'Same sites', weight: '75'},
			                    {idRule:12352, name:'Schedule appointments ASAP', weight: '100'},
			                    {idRule:12353, name:'Schedule non-appointments ASAP', weight: '100'},
			                    {idRule:12354, name:'Task Priority', weight: '400'}];
			$scope.tblSelections = [];
			$scope.tblColumns = [{ name: 'idRule', displayName: 'Id Rule', maxWidth: 80 },
			                     { name: 'name', displayName: 'Name Rule', minWidth: 300 },
			                     { name: 'weight', displayName: 'Weight'}];

			$scope.gridRules = { 
					data: $scope.listRules, 
					columnDefs: $scope.tblColumns,
					selectedItems: $scope.tblSelections,
					showGridFooter: true,
					multiSelect: false };

			$scope.save = function()
			{
				$scope.listRules.push({idRule:$scope.sequence, name:$scope.rule, weight: $scope.weight});
				$scope.rule = '';
				$scope.weight = '';
				$scope.sequence++;
			};
			$scope.edit = function()
			{
				console.info($scope.gridRules.selectedItems);
//				$scope.listRules.push({idRule:$scope.sequence, name:$scope.rule, weight: $scope.weight});
//				$scope.rule = '';
//				$scope.weight = '';
//				$scope.sequence++;
			};
		});


app.controller("ctrlGeneric", function($scope)
{
	
});
