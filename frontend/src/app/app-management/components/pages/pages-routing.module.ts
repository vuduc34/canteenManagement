import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthGuard } from '../../service/auth-guard';
import { ManageAccountComponent } from './manage-account/manage-account.component';
import { AuthGuardAdmin } from '../../service/authGuard-Admin';
import { ActionHistoryComponent } from './action-history/action-history.component';
import { ManageOrderComponent } from './manage-order/manage-order.component';
import { ManageMenuComponent } from './manage-menu/manage-menu.component';

@NgModule({
    imports: [RouterModule.forChild([
        { path: 'empty', loadChildren: () => import('./empty/emptydemo.module').then(m => m.EmptyDemoModule) },
        { path: 'manageAccount', component: ManageAccountComponent, canActivate:[AuthGuard]},
        { path: 'action', component: ActionHistoryComponent, canActivate:[AuthGuard,AuthGuardAdmin]},
        { path: 'manageOrder', component: ManageOrderComponent, canActivate:[AuthGuard]},
        { path: 'manageMenu', component: ManageMenuComponent, canActivate:[AuthGuard]},
    ])],
    exports: [RouterModule]
})
export class PagesRoutingModule { }
