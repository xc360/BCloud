import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';


const routes: Routes = [
  {
    path: 'supplierManage',
    loadChildren: () => import('./supplier-management/supplier-management.module').then(m => m.SupplierManagementModule)
  },
  {
    path: 'messageManage',
    loadChildren: () => import('./message-management/message-management.module').then(m => m.MessageManagementModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApiManagementRoutingModule {
}
