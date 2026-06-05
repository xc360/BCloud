import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthorityListComponent} from './authority-list/authority-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'authorityList', pathMatch: 'full'},
  {path: 'authorityList', component: AuthorityListComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthorityManagementRoutingModule { }
