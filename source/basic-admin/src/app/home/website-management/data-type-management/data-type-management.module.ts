import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {DataTypeManagementRoutingModule} from './data-type-management-routing.module';
import {DataTypeListComponent} from './data-type-list/data-type-list.component';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [DataTypeListComponent],
  imports: [
    CommonModule,
    DataTypeManagementRoutingModule,
    FormsModule,
    NzFormModule,
    TableFindModule,
    FormInfoModule,
    NzSelectModule,
    NzModalModule,
    NzInputModule
  ]
})
export class DataTypeManagementModule {
}
