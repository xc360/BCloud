import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {SupplierListComponent} from './supplier-list/supplier-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'supplierList', pathMatch: 'full'},
  {path: 'supplierList', component: SupplierListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SupplierManagementRoutingModule {
}
