import { CanActivateFn } from "@angular/router";
import { AuthService } from "./auth.service";
import { Router } from "@angular/router";
import { inject } from "@angular/core";

export function featureFlagGuard(
    flagName: string,
    redirectRoute: string
): CanActivateFn {
    return () => {
        const authService: AuthService = inject(AuthService);
        const router: Router = inject(Router);
        const isLoggedIn = authService.isLoggedIn();
        if (flagName === 'login') {
            return !isLoggedIn || router.createUrlTree(['/']);
        }
        return (isLoggedIn && authService.hasRole(flagName)) || router.createUrlTree([redirectRoute]);
    };
}
