import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';


const routes: Routes = [
  {
    path: 'pageManage',
    loadChildren: () => import('./page-management/page-management.module').then(m => m.PageManagementModule)
  },
  {
    path: 'columnManage',
    loadChildren: () => import('./column-management/column-management.module').then(m => m.ColumnManagementModule)
  },
  {
    path: 'dataTypeManage',
    loadChildren: () => import('./data-type-management/data-type-management.module').then(m => m.DataTypeManagementModule)
  }, {
    path: 'menuManage',
    loadChildren: () => import('../website-management/menu-management/menu-management.module').then(m => m.MenuManagementModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WebsiteManagementRoutingModule {
}
