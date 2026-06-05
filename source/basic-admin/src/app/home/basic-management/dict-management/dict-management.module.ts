import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DictManagementRoutingModule } from './dict-management-routing.module';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {FormsModule} from '@angular/forms';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {DictListComponent} from './dict-list/dict-list.component';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [DictListComponent],
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
    DictManagementRoutingModule
  ]
})
export class DictManagementModule { }
