import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {TaskManagementRoutingModule} from './task-management-routing.module';
import {FormsModule} from '@angular/forms';
import {NzButtonModule, NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/common/overall-select/overall-select.module';
import {TaskListComponent} from './task-list/task-list.component';
import {TaskLogComponent} from './task-log/task-log.component';


@NgModule({
  declarations: [TaskListComponent, TaskLogComponent],
  imports: [
    CommonModule,
    TaskManagementRoutingModule,
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
export class TaskManagementModule {
}
