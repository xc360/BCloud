import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthorityDetailsComponent} from './authority-details/authority-details.component';
import {AuthorityAuditComponent} from './authority-audit/authority-audit.component';


const routes: Routes = [
  {path: '', redirectTo: 'list', pathMatch: 'full'},
  {path: 'list', component: AuthorityAuditComponent},
  {path: 'authorityDetails', component: AuthorityDetailsComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthorityRoutingModule {
}
