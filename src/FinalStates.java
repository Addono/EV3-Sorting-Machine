
enum FinalStates implements FinalState {
	Exit {
		@Override
		public FinalState next(Input input) {
			return Exit;
		}
	}
}