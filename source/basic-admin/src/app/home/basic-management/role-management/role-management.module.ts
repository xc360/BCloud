import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RoleManagementRoutingModule } from './role-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {RoleListComponent} from './role-list/role-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [RoleListComponent],
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
    RoleManagementRoutingModule
  ]
})
export class RoleManagementModule { }
