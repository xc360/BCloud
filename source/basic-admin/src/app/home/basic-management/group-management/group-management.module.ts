import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GroupManagementRoutingModule } from './group-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {GroupListComponent} from './group-list/group-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [GroupListComponent],
  imports: [
    CommonModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule,
    OverallSelectModule,
    GroupManagementRoutingModule
  ]
})
export class GroupManagementModule { }
