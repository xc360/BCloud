import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ColumnManagementRoutingModule} from './column-management-routing.module';
import {ColumnListComponent} from './column-list/column-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {FormsModule} from '@angular/forms';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';


@NgModule({
  declarations: [ColumnListComponent],
  imports: [
    CommonModule,
    FormsModule,
    ColumnManagementRoutingModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule
  ]
})
export class ColumnManagementModule {
}
