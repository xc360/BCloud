import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AuthorizeListComponent} from './authorize-list/authorize-list.component';
import {AuthorizeAuthorityListComponent} from './authorize-authority-list/authorize-authority-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'authorizeList', pathMatch: 'full'},
  {path: 'authorizeList', component: AuthorizeListComponent},
  {path: 'authorizeAuthorityList', component: AuthorizeAuthorityListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthorizeManagementRoutingModule {
}
