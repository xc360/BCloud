import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ColumnListComponent} from './column-list/column-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'columnList', pathMatch: 'full'},
  {path: 'columnList', component: ColumnListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ColumnManagementRoutingModule {
}
