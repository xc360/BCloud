import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {InfoListComponent} from './info-list/info-list.component';
import {InfoEditComponent} from './info-edit/info-edit.component';

const routes: Routes = [
  {path: '', redirectTo: 'infoList', pathMatch: 'full'},
  {path: 'infoList', component: InfoListComponent},
  {path: 'infoEdit', component: InfoEditComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InfoManagementRoutingModule {
}
