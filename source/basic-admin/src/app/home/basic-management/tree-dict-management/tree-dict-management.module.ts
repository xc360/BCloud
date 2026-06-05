import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TreeDictManagementRoutingModule } from './tree-dict-management-routing.module';
import { TreeDictListComponent } from './tree-dict-list/tree-dict-list.component';
import {FormsModule} from '@angular/forms';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../../projects/xc-common/src/public-api';
import {OverallSelectModule} from '../../../../../projects/xc-common/src/public-api';

@NgModule({
  declarations: [TreeDictListComponent],
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
    TreeDictManagementRoutingModule
  ]
})
export class TreeDictManagementModule { }
