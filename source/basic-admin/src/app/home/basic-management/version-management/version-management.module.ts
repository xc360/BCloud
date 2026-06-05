import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {VersionManagementRoutingModule} from './version-management-routing.module';
import {VersionListComponent} from './version-list/version-list.component';
import {VersionDocComponent} from './version-doc/version-doc.component';
import {FormsModule} from '@angular/forms';
import {NzFormModule, NzInputModule, NzModalModule, NzSelectModule} from 'ng-zorro-antd';
import {TableFindModule} from '../../../../../projects/xc-common/src/common/table-find/table-find.module';
import {FormInfoModule} from '../../../../../projects/xc-common/src/common/form-info/form-info.module';
import {NzMessageModule} from 'ng-zorro-antd/message';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {PipeModule} from '../../../../../projects/xc-common/src/common/pipe/pipe.module';
import {NzIconModule} from 'ng-zorro-antd/icon';


@NgModule({
  declarations: [VersionListComponent, VersionDocComponent],
  imports: [
    CommonModule,
    TableFindModule,
    FormInfoModule,
    NzMessageModule,
    NzModalModule,
    FormsModule,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzSelectModule,
    PipeModule,
    NzIconModule,
    VersionManagementRoutingModule
  ]
})
export class VersionManagementModule {
}
