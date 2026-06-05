import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AppAuditComponent} from './app-audit/app-audit.component';
import {AppDetailsComponent} from './app-details/app-details.component';


const routes: Routes = [
  {path: '', redirectTo: 'list', pathMatch: 'full'},
  {path: 'list', component: AppAuditComponent},
  {path: 'appDetails', component: AppDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
