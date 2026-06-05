import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from './user-list/user-list.component';
import {UserDetailsComponent} from './user-details/user-details.component';

const routes: Routes = [
  {path: '', redirectTo: 'userList', pathMatch: 'full'},
  {path: 'userList', component: UserListComponent},
  {path: 'userDetails', component: UserDetailsComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserManagementRoutingModule {
}
