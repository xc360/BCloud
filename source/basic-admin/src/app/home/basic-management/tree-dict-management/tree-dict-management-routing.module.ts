import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {TreeDictListComponent} from './tree-dict-list/tree-dict-list.component';

const routes: Routes = [
  {path: '', redirectTo: 'treeDictList', pathMatch: 'full'},
  {path: 'treeDictList', component: TreeDictListComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TreeDictManagementRoutingModule { }
