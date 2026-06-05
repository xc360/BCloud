import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {PageListComponent} from './page-list/page-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'pageList', pathMatch: 'full'},
  {path: 'pageList', component: PageListComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PageManagementRoutingModule {
}
