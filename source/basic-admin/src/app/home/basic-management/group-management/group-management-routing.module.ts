import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {GroupListComponent} from './group-list/group-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'groupList', pathMatch: 'full'},
  {path: 'groupList', component: GroupListComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GroupManagementRoutingModule { }
