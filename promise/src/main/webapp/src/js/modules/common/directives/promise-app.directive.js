/**=========================================================
 * Module: promise-app.js
 * App promise, include: chart, select and etc...
 =========================================================*/

(function () {
  'use strict';

  angular
    .module('naut')
    .directive('promiseChart', function () {
      return {
        restrict: 'AE',
        transclude: {
          slotOne: 'p',
          slotTwo: 'div',
        },
        scope: {
          type: '@', // charts type. e.g: 'bar', 'doughnut', and 'pie'
          // datasets. e.g: { data, dataLabels, labels, title, options }
          // data: array data, an example: [[1,2,3], [2,3,4], [7,8,9], [10,11,12]]
          // dataLabels: labels for each data[n] in array. e.g: ['dataOne', 'datatwo', 'dataThreee'] = data[i].length
          // labels: for each data in array. e.g: ['Januari', 'Februari', 'Maret', 'April'] = data.length
          // title: chart title
          // options: google charts options => You can look at the documentation of google charts: https://developers.google.com/chart/interactive/docs
          datasets: '=',
          // change: handleChange. e.g: <promise-chart on-change="myHandleChangeInMyController"></promise-chart>
          // so, if you're using filter on this chart, make sure to add 'on-change'.
          change: '&onChange',
        },
        // templateUrl: './template.html',
        template: `
            <div class="promise-charts-wrapper">
              <div class="loading" ng-if="isLoading">
                <div class="lds-ellipsis"><div></div><div></div><div></div><div></div></div>
              </div>
              <div ng-if="!isLoading">
                <h4>{{ datasets.title }}</h4>
                <ng-transclude ng-if="isTransclude"></ng-transclude>
                <div ng-if="datasets.filterItem" style="width: 100%; height: 32px">
                  <promise-select 
                    on-change="handleChange" 
                    ng-model="someModel" 
                    options="datasets.filterItem.data" 
                    view-field="{{datasets.filterItem.viewField}}" 
                    value-field="{{datasets.filterItem.valueField}}" 
                    custom-class="promise-charts"
                    ng-if="!isTransclude"></promise-select>
                </div>
                <div ng-if="type === 'bar' || type === 'line'" style="margin-top: 4rem">
                  <div ng-attr-id="{{generatedId}}" style="width: 100% !important; height: 166px;" ></div>
                </div>
                <div ng-if="type === 'pie' || type === 'doughnut'">
                  <div ng-attr-id="{{generatedId}}" style="width: 100% !important;"></div>
                </div>
              </div>
            </div>
          `,
        link: function ($scope, elmnt, attrs, ctr, $transclude) {
          $scope.isTransclude = false;
          $scope.chartOptionsData;
          $scope.isEmpty = true;
          $scope.chart = [];
          $scope.isLoading = true;

          $scope.generateRandomId = function (length = 36) {
            let result = '';
            const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            const charactersLength = characters.length;
            for (let i = 0; i < length; i++) {
              result += characters.charAt(Math.floor(Math.random() * charactersLength));
            }
            return result;
          };

          $scope.generatedId = $scope.generateRandomId();
          // set if transclude exists
          $transclude(function (clone) {
            if (clone.length > 0) {
              $scope.isTransclude = true;
            }
          });

          // handle change promise select
          $scope.handleChange = function (dataSelected) {
            $scope.isLoading = true;
            if (typeof $scope.change() === 'function') {
              $scope.change()($scope.generatedId, dataSelected);
            }
          };

          $scope.setIsEmptyData = function (dataArray) {
            dataArray.forEach((data) => {
              if (Array.isArray(data)) {
                data.forEach((d) => {
                  if (!this.isEmpty) return;
                  if (d === 'null' || d !== 0) {
                    this.isEmpty = false;
                  }
                });
              } else {
                if (data === 'null' || data !== 0) {
                  this.isEmpty = false;
                }
              }
            });
          };

          $scope.init = function () {
            $scope.setChart();
          };

          // set bar chart
          $scope.setChart = function () {
            if (!Array.isArray($scope.datasets.data)) {
              return;
            } else {
              this.setIsEmptyData($scope.datasets.data);
            }

            $scope.setIsEmptyData($scope.datasets.data);

            if ($scope.type === 'bar') {
              google.charts.load('current', { packages: ['corechart', 'bar'] });
            } else if ($scope.type === 'pie' || $scope.type === 'doughnut') {
              google.charts.load('current', { packages: ['corechart'] });
            } else if ($scope.type === 'line') {
              google.charts.load('current', { packages: ['corechart'] });
            }

            google.charts.setOnLoadCallback($scope.drawChart);

            // add labels name to datalabels
            $scope.datasets.dataLabels.unshift($scope.datasets.labelsName || '');

            // add labels to data
            for (let i = 0; i < $scope.datasets.labels.length; i++) {
              $scope.datasets.data[i].unshift($scope.datasets.labels[i]);
            }

            // add dataLabels to data
            $scope.datasets.data.unshift($scope.datasets.dataLabels);
          };

          // draw bar-chart canvas
          $scope.drawChart = function () {
            if ($scope.isEmpty) {
              //$scope.datasets.data = $scope.datasets.data.slice(0, 1);
            }
            var data = google.visualization.arrayToDataTable($scope.datasets.data);
            if ($scope.type === 'bar') {
              $scope.chart[$scope.generatedId] = new google.charts.Bar(document.getElementById($scope.generatedId));
              $scope.chart[$scope.generatedId].draw(data, google.charts.Bar.convertOptions($scope.chartOptionsData));
            } else if ($scope.type === 'pie' || $scope.type === 'doughnut') {
              $scope.chart[$scope.generatedId] = new google.visualization.PieChart(document.getElementById($scope.generatedId));
              $scope.chart[$scope.generatedId].draw(data, $scope.chartOptionsData);
            } else if ($scope.type === 'line') {
              $scope.chart[$scope.generatedId] = new google.visualization.LineChart(document.getElementById($scope.generatedId));
              $scope.chart[$scope.generatedId].draw(data, $scope.chartOptionsData);
            }
          };

          $scope.chartOptions = function () {
            if ($scope.type === 'bar') {
              $scope.chartOptionsData = {
                legend: { position: 'none' },
                vAxis: {
                  textPosition: 'none',
                  gridlines: {
                    color: 'transparent',
                  },
                },
              };
            } else if ($scope.type === 'pie' || $scope.type === 'doughnut') {
              $scope.chartOptionsData = {
                sliceVisibilityThreshold: 0,
                legend: { position: 'right' },
              };

              if ($scope.type === 'doughnut') {
                Object.assign($scope.chartOptionsData, { pieHole: 0.4 });
              }
            } else if ($scope.type === 'line') {
              $scope.chartOptionsData = {
                legend: { position: 'bottom' },
                curveType: 'function',
              };
            }

            if ($scope.datasets.options) {
              $scope.replaceObjectKeysValue($scope.chartOptionsData, $scope.datasets.options);
            }
          };

          // now, this is not working
          $scope.replaceObjectKeysValue = function (baseObject, othersObject) {
            const objectKeys = Object.keys(othersObject);
            if (objectKeys.length > 0) {
              objectKeys.forEach((key, index) => {
                if (!baseObject.hasOwnProperty(key)) {
                  baseObject[key] = {};
                  baseObject[key] = othersObject[key];
                } else {
                  if (typeof othersObject[key] === 'object') {
                    $scope.replaceObjectKeysValue(baseObject[key], othersObject[key]);
                  } else {
                    baseObject[key] = othersObject[key];
                  }
                }
              });
            }
          };

          $scope.$watch('datasets', function (value) {
            $scope.isLoading = true;
            if (typeof value === 'undefined' || value === null) {
              return;
            }
            $scope.isLoading = false;
            $scope.chartOptions();
            $scope.init();
          });
        },
      };
    })
    .directive('promiseSelect', function () {
      return {
        restrict: 'AE',
        transclude: true,
        scope: {
          viewField: '@', // view in option. e.g: 'name'
          valueField: '@', // value in option. e.g: 'id'. default is an object of options
          customClass: '@', // custom class for styling the select. e.g: 'my-class'
          options: '=', // options is an array object. e.g: data = [{ id: 1, name: 'Option: 1'}, { id: 2, name: 'Option 2'} ]
          ngModel: '=', // ngModel
          change: '&onChange', // change: handleChange. e.g: <promise-select on-change="myHandleChangeInMyController"></promise-select>
          placeholder: '@', // input placeholder
        },
        link: function ($scope, elmnt, attrs, ctr) {
          $scope.handleChange = function (model) {
            try {
              $scope.change()(JSON.parse(model));
            } catch (ex) {
              $scope.change()(model);
            }
          };
        },
        template: `
            <div class="directive-select" ng-class="customClass ? customClass : ''">
              <select
                class="form-control"
                ng-model="ngModel" 
                ng-change="handleChange(ngModel)">
                <option selected value="" selected disabled>{{placeholder || 'Select'}}</option>
                <option ng-repeat="option in options" value="{{valueField ? option[valueField] : option}}">{{viewField ? option[viewField] : option}}</option>
              </select>
            </div>
          `,
      };
    })
    .directive('promiseTree', [
      '$compile',
      function ($compile) {
        return {
          restrict: 'AE',
          transclude: true,
          scope: {
            treeList: '=',
            click: '&onClick',
            viewValue: '@',
            filterName: '@',
          },
          // templateUrl: './template.html',
          template: `
          <div class="button-group text-right">
            <div style="border-bottom: white;"> 
              <strong style="float: left;" translate="{{filterName}}" class="ng-scope"></strong>
						</div>
            <button class="btn btn-primary btn-xs" ng-click="handleExpandAll()">Expand All</button>
            <button class="btn btn-primary btn-xs" ng-click="handleCollapseAll()">Collapse All</button>
          </div>
          <div class="input-group" style="width: 100%; position: relative; margin: 1rem 0">
            <input type="text" class="form-control" ng-change="handleChange()" ng-model="searchValue">
            <span class="glyphicon glyphicon-search" style="position: absolute; right: 0; top:0;"></span>
          </div>
          <div class="promise-tree" ng-attr-id="{{generatedId}}" style="width: 100% !important;" ></div>
          <ng-transclude></ng-transclude>`,
          link: function ($scope) {
            $scope.treeListTemp = [];
            $scope.searchValue = null;
            $scope.htmlElement = '';

            $scope.saveTreeListItemToTemp = function (treeListItem) {
              treeListItem.forEach((item) => {
                $scope.treeListTemp.push(item);
                if (item.categoryChildList !== null && item.categoryChildList.length > 0) {
                  $scope.saveTreeListItemToTemp(item.categoryChildList);
                }
              });
            };

            $scope.generateRandomId = function (length = 36) {
              let result = '';
              const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
              const charactersLength = characters.length;
              for (let i = 0; i < length; i++) {
                result += characters.charAt(Math.floor(Math.random() * charactersLength));
              }
              return result;
            };

            $scope.generatedId = $scope.generateRandomId();

            $scope.buildTreeList = function (element, treeList) {
              element = '<ul>';
              treeList.forEach((item) => {
                element += '<li>';
                element += `<span ng-click="handleClick($event, ${item.id})">`;
                element += $scope.viewValue ? item[$scope.viewValue] : item.translateInd;
                element += '</span>';
                if (item.categoryChildList !== null && item.categoryChildList.length > 0) {
                  element += $scope.buildTreeList(element, item.categoryChildList);
                }
                element += '</li>';
              });
              element += '</ul>';
              return element;
            };

            $scope.handleClick = function (event, itemId) {
              event.preventDefault();
              event.stopPropagation();
              const result = $scope.treeListTemp.filter((i) => i.id == itemId)[0];
              if (typeof $scope.click() === 'function') {
                $scope.click()(result);
              }
            };

            $scope.handleChange = function () {
              $($scope.ulId).empty();
              const filterResults = $scope.filterTreeList(angular.copy($scope.treeList));
              const element = $scope.buildTreeList($scope.htmlElement, $scope.searchValue === '' ? $scope.treeList : filterResults);
              $scope.compileElement = $compile(element)($scope);
              $scope.buildTreeListHtml();
              this.searchValue === '' ? this.handleCollapseAll() : $scope.handleExpandAll();
            };

            $scope.filterTreeList = function (arrayTree) {
              arrayTree = arrayTree.filter((data) => {
                if (data.categoryChildList !== null && data.categoryChildList.length > 0) {
                  data.categoryChildList = $scope.filterTreeList(data.categoryChildList);
                }

                const dataValue = $scope.viewValue ? data[$scope.viewValue] : data.translateInd;

                if (data.categoryChildList === null) {
                  if (dataValue.toLowerCase().indexOf($scope.searchValue.toLowerCase()) !== -1) {
                    return data;
                  }
                } else {
                  if (data.categoryChildList.length > 0) {
                    return data;
                  } else {
                    if (dataValue.toLowerCase().indexOf($scope.searchValue.toLowerCase()) !== -1) {
                      return data;
                    }
                  }
                }
              });
              return arrayTree;
            };

            $scope.handleExpandAll = function () {
              $('.collapsed').addClass('expanded');
              $('.collapsed').children('ul').show('hight');
            };

            $scope.handleCollapseAll = function () {
              $('.collapsed').removeClass('expanded');
              $('.collapsed').children('ul').hide('hight');
            };

            $scope.buildTreeListHtml = function () {
              $($scope.ulId)
                .append($scope.compileElement)
                .find('li:has(ul)')
                .click(function () {
                  event.preventDefault();
                  event.stopPropagation();
                  const parentElement = $(this.parentElement);
                  const spanElement = parentElement.find('.collapsed.expanded');
                  if (spanElement[0] && spanElement[0] !== event.target) {
                    spanElement.toggleClass('expanded');
                    $(parentElement).find('ul').hide('medium');
                  }
                  if (this == event.target) {
                    $(this).toggleClass('expanded');
                    $(this).children('ul').slideToggle('slow');
                  }
                })
                .addClass('collapsed')
                .children('ul')
                .hide();
            };

            $scope.$watch('treeList', function (value) {
              if (typeof value !== 'undefined') {
                $scope.saveTreeListItemToTemp($scope.treeList);
                $scope.treeElement = $scope.buildTreeList($scope.htmlElement, $scope.treeList);
                $scope.ulId = '#' + $scope.generatedId;
                $scope.compileElement = $compile($scope.treeElement)($scope);
                $scope.buildTreeListHtml();
              }
            });
          },
        };
      },
    ])
    .directive('promiseMultipleSelect', [
      '$compile',
      function ($compile) {
        return {
          restrict: 'AE',
          transclude: true,
          require: 'ngModel',
          scope: {
            viewField: '@', // view in option. e.g: 'name'
            valueField: '@', // value in option. e.g: 'id'. default is an object of options
            customClass: '@', // custom class for styling the select. e.g: 'my-class'
            optionList: '=', // options is an array object. e.g: data = [{ id: 1, name: 'Option: 1'}, { id: 2, name: 'Option 2'} ]
            ngModel: '=', // ngModel
            change: '&onChange', // change: handleChange. e.g: <promise-multiple-select on-change="myHandleChangeInMyController"></promise-multiple-select>
            placeholder: '@', // input placeholder
          },
          template:
            '<div class="promise-multiple-select" ng-class="customClass ? customClass : \'\'">' +
            '<label class="form-control value-label" id="{{ \'value-label-\' + generatedId}}" ng-click="handleClick($event)"></label>' +
            '<div class="custom-select-wrapper" id="{{\'custom-select-wrapper-\' + generatedId}}">' +
            '</div>' +
            '</div>',
          link: function ($scope, element, attrs, ngModelCtrl) {
            if (!$scope.ngModel) {
              $scope.ngModel = [];
            }

            $scope.generateRandomId = function (length = 4) {
              let result = '';
              const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
              const charactersLength = characters.length;
              for (let i = 0; i < length; i++) {
                result += characters.charAt(Math.floor(Math.random() * charactersLength));
              }
              return result;
            };
            $scope.generatedId = $scope.generateRandomId();

            $scope.handleClick = function (event) {
              event.stopPropagation();
              const customSelectOuterWrapperElement = document.getElementById('custom-select-wrapper-' + this.generatedId);
              customSelectOuterWrapperElement.classList.toggle('show');
              if (customSelectOuterWrapperElement.children.length === 0) {
                $scope.buildCustomSelectHeaderElement();
                $scope.buildSelectElementChildren();
              }
            };

            $scope.buildCustomSelectHeaderElement = function () {
              const customSelectWrapperElement = document.getElementById('custom-select-wrapper-' + $scope.generatedId);
              const divCustomSelectHeaderElement = document.createElement('div');
              divCustomSelectHeaderElement.className = 'custom-select-header';
              const divCheckBoxGroupElement = document.createElement('div');
              divCheckBoxGroupElement.classList.add('check-box-group');
              const inputCheckboxAllElement = document.createElement('input');
              inputCheckboxAllElement.type = 'checkbox';
              inputCheckboxAllElement.id = 'check-box-all-' + $scope.generatedId;
              inputCheckboxAllElement.addEventListener('click', (event) => {
                $scope.handleCheckAllItem(event.target.checked);
              });
              const labelCheckBoxAllElement = document.createElement('label');
              labelCheckBoxAllElement.htmlFor = 'check-box-all-' + $scope.generatedId;
              labelCheckBoxAllElement.innerText = 'Check All';
              labelCheckBoxAllElement.addEventListener('click', (event) => {
                event.stopPropagation();
                event.preventDefault();
                const inputCheckboxAllElement = document.getElementById('check-box-all-' + $scope.generatedId);
                inputCheckboxAllElement.checked = !inputCheckboxAllElement.checked;
                $scope.handleCheckAllItem(inputCheckboxAllElement.checked);
              });
              divCheckBoxGroupElement.appendChild(inputCheckboxAllElement);
              divCheckBoxGroupElement.appendChild(labelCheckBoxAllElement);
              divCustomSelectHeaderElement.appendChild(divCheckBoxGroupElement);
              // <button class="btn btn-primary btn-sm">Submit</button>
              const buttonElement = document.createElement('button');
              buttonElement.className = 'btn btn-primary btn-sm';
              buttonElement.innerText = 'Apply';
              divCustomSelectHeaderElement.appendChild(buttonElement);
              customSelectWrapperElement.appendChild(divCustomSelectHeaderElement);

              buttonElement.addEventListener('click', (event) => {
                $scope.closeCustomSelectElement();
                $scope.emitChange('click');
              });

              divCustomSelectHeaderElement.addEventListener('click', (event) => {
                event.stopPropagation();
              });
            };

            // builder section
            $scope.buildSelectElementChildren = function () {
              const customSelectWrapperElement = document.getElementById('custom-select-wrapper-' + this.generatedId);
              const ulCustomSelectElement = document.createElement('ul');
              ulCustomSelectElement.id = 'custom-select-' + this.generatedId;
              ulCustomSelectElement.className = 'custom-select';
              const valueField = $scope.valueField;
              for (let i = 0; i < $scope.optionList.length; i++) {
                const item = $scope.optionList[i];
                const isItemSelected = $scope.ngModel.findIndex((i) => (valueField ? i.toString() : JSON.stringify(i)) === (valueField ? item[valueField].toString() : JSON.stringify(item))) !== -1;
                const liElement = document.createElement('li');
                liElement.classList.add('custom-select-item');
                const inputCheckBoxElement = document.createElement('input');
                inputCheckBoxElement.type = 'checkbox';
                inputCheckBoxElement.id = 'input' + i + '-' + $scope.generatedId;
                if (isItemSelected) {
                  inputCheckBoxElement.checked = true;
                }
                const spanElement = document.createElement('span');
                spanElement.classList.add('custom-select-item-text');
                spanElement.innerText = $scope.viewField ? item[$scope.viewField] : item.name || item;
                liElement.appendChild(inputCheckBoxElement);
                liElement.appendChild(spanElement);
                ulCustomSelectElement.appendChild(liElement);
                customSelectWrapperElement.appendChild(ulCustomSelectElement);

                // event listener section
                inputCheckBoxElement.addEventListener('click', (event) => {
                  event.stopPropagation();
                  $scope.handleListener(event.target, item);
                });

                liElement.addEventListener('click', (event) => {
                  event.stopPropagation();
                  inputCheckBoxElement.checked = !inputCheckBoxElement.checked;
                  $scope.handleListener(inputCheckBoxElement, item);
                });
              }
              $scope.handleCheckAllUnchecked();
            };

            $scope.handleCheckAllItem = (isChecked) => {
              const customSelectElement = document.getElementById('custom-select-' + $scope.generatedId);
              const customSelectChildrenLength = customSelectElement.children.length;
              if (customSelectChildrenLength > 0) {
                for (let i = 0; i < customSelectChildrenLength; i++) {
                  const inputElement = document.getElementById('input' + i + '-' + $scope.generatedId);
                  if (inputElement) {
                    inputElement.checked = isChecked;
                  }
                }
              }
              const valueField = $scope.valueField;
              const newOptionList = Array.from($scope.optionList);
              $scope.ngModel = isChecked ? (valueField ? newOptionList.map((option) => option[valueField]) : newOptionList) : [];
              $scope.setValueLabelText();
              $scope.emitChange('change');
            };

            $scope.handleListener = (element, item) => {
              const valueField = $scope.valueField;
              if (element.checked) {
                $scope.ngModel.push(valueField ? item[valueField] : item);
                $scope.setValueLabelText();
              } else {
                $scope.removeItemFromNgModel(item);
                $scope.setValueLabelText();
              }
              $scope.handleCheckAllUnchecked();
              $scope.emitChange('change');
            };

            $scope.handleCheckAllUnchecked = function () {
              const customSelectElement = document.getElementById('custom-select-' + $scope.generatedId);
              const checkAllInputElement = document.getElementById('check-box-all-' + $scope.generatedId);
              let isCheckAll = true;
              for (let i = 0; i < customSelectElement.children.length; i++) {
                const inputElement = document.getElementById('input' + i + '-' + $scope.generatedId);
                if (inputElement) {
                  if (!inputElement.checked && isCheckAll) {
                    isCheckAll = false;
                  }
                }
              }
              checkAllInputElement.checked = isCheckAll;
            };

            $scope.removeItemFromNgModel = function (item) {
              const valueField = $scope.valueField;
              const indexOfItem = $scope.ngModel.findIndex((i) => ((valueField ? i[valueField] : JSON.stringify(i)) === valueField ? i[valueField] : JSON.stringify(item)));
              $scope.ngModel.splice(indexOfItem, 1);
            };

            $scope.setValueLabelText = function () {
              const valueLabelElement = document.getElementById('value-label-' + $scope.generatedId);
              const ngModelValueLength = $scope.NgModel && $scope.NgModel.length;
              const viewField = $scope.viewField;
              const valueField = $scope.valueField;
              let placeHolderText = $scope.placeholder ? $scope.placeholder : 'Select';
              if (ngModelValueLength && ngModelValueLength > 0) {
                if (ngModelValueLength === 1 && valueField) {
                  const indexOfValue = $scope.optionList.findIndex((i) => i[valueField] === $scope.ngModel[0]);
                  const value = $scope.optionList[indexOfValue];
                  placeHolderText = viewField ? value[viewField] : value.name || value;
                } else {
                  placeHolderText = ngModelValueLength === 1 ? (viewField && $scope.ngModel[0][viewField]) || $scope.ngModel[0].name : ngModelValueLength + ' selected';
                }
              }
              if (valueLabelElement) {
                valueLabelElement.innerText = placeHolderText;
              }
            };

            $scope.emitChange = function (eventName) {
              ngModelCtrl.$setViewValue($scope.ngModel);
              ngModelCtrl.$render();
              $scope.$apply();
              if (typeof $scope.change() === 'function') {
                $scope.change()(eventName, $scope.ngModel);
              }
            };

            $scope.closeCustomSelectElement = function () {
              const customSelectElement = document.getElementById('custom-select-wrapper-' + $scope.generatedId);
              if (customSelectElement && customSelectElement.classList.contains('show')) {
                customSelectElement.classList.remove('show');
              }
            };

            document.addEventListener('click', (event) => {
              $scope.closeCustomSelectElement();
            });

            angular.element(document).ready(function () {
              $scope.setValueLabelText();
            });

            $scope.$watch('optionList', function (value) {
              $scope.setValueLabelText();
            });
          },
        };
      },
    ]);
})();
