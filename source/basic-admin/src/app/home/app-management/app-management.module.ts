import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
// 阿里ui
import {NzMessageModule} from 'ng-zorro-antd/message';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzIconModule} from 'ng-zorro-antd/icon';
// 自己的组件
import {AppManagementRoutingModule} from './app-management-routing.module';
import {TableFindModule} from '../../../../projects/xc-common/src/public-api';
import {AppListComponent} from './app-list/app-list.component';
import {AuthorityListComponent} from './authority-list/authority-list.component';
import {PipeModule} from '../../../../projects/xc-common/src/public-api';
import {FormInfoModule} from '../../../../projects/xc-common/src/public-api';


@NgModule({
  declarations: [
    AppListComponent,
    AuthorityListComponent,
  ],
  imports: [
    CommonModule,
    AppManagementRoutingModule,
    TableFindModule,
    NzMessageModule,
    NzModalModule,
    FormsModule,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzSelectModule,
    PipeModule,
    NzIconModule,
    FormInfoModule
  ]
})
export class AppManagementModule {
}
