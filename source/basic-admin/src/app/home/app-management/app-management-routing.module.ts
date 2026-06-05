import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppListComponent} from './app-list/app-list.component';
import {AuthorityListComponent} from './authority-list/authority-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'appList', pathMatch: 'full'},
  {path: 'appList', component: AppListComponent},
  {path: 'appAuthorityList', component: AuthorityListComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppManagementRoutingModule {
}
