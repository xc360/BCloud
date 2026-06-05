import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {TaskLogComponent} from './task-log/task-log.component';
import {TaskListComponent} from './task-list/task-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'taskList', pathMatch: 'full'},
  {path: 'taskList', component: TaskListComponent},
  {path: 'taskLog', component: TaskLogComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TaskManagementRoutingModule {
}
