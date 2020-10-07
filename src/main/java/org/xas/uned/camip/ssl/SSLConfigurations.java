package org.xas.uned.camip.ssl;

@SuppressWarnings("unused")
public class SSLConfigurations {

	private SSLConfigurations() {

	}

	/**
	 * By default we validate and we don't trust certificates
	 */
	public static SSLConfiguration byDefault() {
		return validate(true);
	}

	public static SSLConfiguration validate(final boolean validate) {
		return validate(is(validate));
	}

	public static SSLConfiguration validate(final Provider<Boolean> validate) {
		return DefaultSSLConfiguration.builder().validate(validate).build();
	}

	public static SSLConfiguration verifyHostnameAndIsTrustedHost(final boolean verifyHostname,
			final boolean trustedHost) {
		return verifyHostnameAndIsTrustedHost(is(verifyHostname), is(trustedHost));
	}

	public static SSLConfiguration verifyHostnameAndIsTrustedHost(final Provider<Boolean> verifyHostname,
			final Provider<Boolean> trustedHost) {
		return DefaultSSLConfiguration.builder().verifyHostname(verifyHostname).trustedHost(trustedHost).build();
	}

	private static Provider<Boolean> is(final boolean value) {
		return value ? isTrue() : isFalse();
	}

	private static Provider<Boolean> not(final Provider<Boolean> provider) {
		return new NotProvider(provider);
	}

	private static Provider<Boolean> isTrue() {
		return new TrueProvider();
	}

	private static Provider<Boolean> isFalse() {
		return new FalseProvider();
	}

	public interface Provider<T> {
		T get();
	}

	private static class NotProvider implements Provider<Boolean> {
		private final Provider<Boolean> provider;

		private NotProvider(Provider<Boolean> provider) {
			this.provider = provider;
		}

		public Boolean get() {
			return !this.provider.get();
		}
	}

	private static class TrueProvider implements Provider<Boolean> {
		public Boolean get() {
			return true;
		}
	}

	private static class FalseProvider implements Provider<Boolean> {
		public Boolean get() {
			return false;
		}
	}

	private static class DefaultSSLConfiguration implements SSLConfiguration {

		private final Provider<Boolean> verifyHostname;

		private final Provider<Boolean> trustedHost;

		private DefaultSSLConfiguration(final Provider<Boolean> verifyHostname, final Provider<Boolean> trustedHost) {
			this.verifyHostname = verifyHostname;
			this.trustedHost = trustedHost;
		}

		public boolean isVerifyHostname() {
			return verifyHostname.get();
		}

		public boolean isTrustedHost() {
			return trustedHost.get();
		}

		public static Builder builder() {
			return new Builder();
		}

		private static class Builder {

			private Provider<Boolean> verifyHostname = isFalse();

			private Provider<Boolean> trustedHost = isFalse();

			private Builder verifyHostname(final Provider<Boolean> verifyHostname) {
				this.verifyHostname = verifyHostname;
				return this;
			}

			private Builder trustedHost(final Provider<Boolean> trustedHost) {
				this.trustedHost = trustedHost;
				return this;
			}

			private Builder validate(final Provider<Boolean> validate) {
				return this.verifyHostname(validate).trustedHost(not(validate));
			}

			private DefaultSSLConfiguration build() {
				return new DefaultSSLConfiguration(verifyHostname, trustedHost);
			}
		}
	}

}
