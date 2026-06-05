import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MessageLogComponent} from './message-log/message-log.component';
import {TemplateListComponent} from './template-list/template-list.component';


const routes: Routes = [
  {path: '', redirectTo: 'templateList', pathMatch: 'full'},
  {path: 'templateList', component: TemplateListComponent},
  {path: 'messageLog', component: MessageLogComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MessageManagementRoutingModule {
}
