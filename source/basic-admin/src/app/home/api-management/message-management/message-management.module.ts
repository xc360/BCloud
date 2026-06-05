import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {MessageManagementRoutingModule} from './message-management-routing.module';
import {FormsModule} from '@angular/forms';
import {NzButtonModule, NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/common/overall-select/overall-select.module';
import {TemplateListComponent} from './template-list/template-list.component';
import {MessageLogComponent} from './message-log/message-log.component';


@NgModule({
  declarations: [TemplateListComponent, MessageLogComponent],
  imports: [
    CommonModule,
    MessageManagementRoutingModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule,
    NzButtonModule,
    OverallSelectModule
  ]
})
export class MessageManagementModule {
}
