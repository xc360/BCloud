import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DataTypeListComponent} from './data-type-list/data-type-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'dataTypeList', pathMatch: 'full'},
  {path: 'dataTypeList', component: DataTypeListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DataTypeManagementRoutingModule {
}
