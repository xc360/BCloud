import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: 'treeDictManage',
    loadChildren: () => import('./tree-dict-management/tree-dict-management.module').then(m => m.TreeDictManagementModule)
  }, {
    path: 'authorityManage',
    loadChildren: () => import('./authority-management/authority-management.module').then(m => m.AuthorityManagementModule)
  }, {
    path: 'dictManage',
    loadChildren: () => import('./dict-management/dict-management.module').then(m => m.DictManagementModule)
  }, {
    path: 'groupManage',
    loadChildren: () => import('./group-management/group-management.module').then(m => m.GroupManagementModule)
  }, {
    path: 'infoManage',
    loadChildren: () => import('./info-management/info-management.module').then(m => m.InfoManagementModule)
  }, {
    path: 'roleManage',
    loadChildren: () => import('./role-management/role-management.module').then(m => m.RoleManagementModule)
  }, {
    path: 'userManage',
    loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule)
  }, {
    path: 'versionManage',
    loadChildren: () => import('./version-management/version-management.module').then(m => m.VersionManagementModule)
  }, {
    path: 'taskManage',
    loadChildren: () => import('./task-management/task-management.module').then(m => m.TaskManagementModule)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BasicManagementRoutingModule {
}
