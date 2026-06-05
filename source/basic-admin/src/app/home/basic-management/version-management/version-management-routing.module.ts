import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {VersionListComponent} from './version-list/version-list.component';
import {VersionDocComponent} from './version-doc/version-doc.component';


const routes: Routes = [
  {path: '', redirectTo: 'versionList', pathMatch: 'full'},
  {path: 'versionList', component: VersionListComponent},
  {path: 'versionDoc', component: VersionDocComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VersionManagementRoutingModule {
}
