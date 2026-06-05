import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {DictListComponent} from './dict-list/dict-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'dictList', pathMatch: 'full'},
  {path: 'dictList', component: DictListComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DictManagementRoutingModule { }
